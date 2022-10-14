package com.example.ms.module.member.service;

import com.example.ms.model.SearchParam;
import com.example.ms.module.member.model.po.MemberLevel;
import com.example.ms.module.member.repository.MemberLevelRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class MemberLevelService {

    private final MemberLevelRepository memberLevelRepository;

    public Page<MemberLevel> search(SearchParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize(), Sort.Direction.ASC, "id");
        String keyword = param.getKeyword();
        if (keyword == null || "".equals(keyword)) {
            return memberLevelRepository.findByDeletedFlag(false, pageable);
        }
        return memberLevelRepository.findByDeletedFlagAndLevelNameLike(false, "%" + keyword + "%", pageable);
    }

    public MemberLevel create(MemberLevel memberLevel) throws Exception {
        MemberLevel oldMemberLevel = memberLevelRepository.findByLevelName(memberLevel.getLevelName());
        if (oldMemberLevel != null && !oldMemberLevel.getDeletedFlag()) {
            throw new Exception("该会员等级已存在");
        }
        if (oldMemberLevel != null && oldMemberLevel.getDeletedFlag()) {
            memberLevel.setId(oldMemberLevel.getId());
        }
        memberLevel.setDeletedFlag(false);
        memberLevel.setCreatedTime(new Date());
        return memberLevelRepository.saveAndFlush(memberLevel);
    }

    public MemberLevel update(MemberLevel memberLevel) throws Exception {
        if (memberLevel.getId() == null || memberLevel.getId() <= 0) {
            throw new Exception("更新会员等级异常");
        }
        Optional<MemberLevel> optional = memberLevelRepository.findById(memberLevel.getId());
        optional.orElseThrow(() -> new Exception("找不到该会员等级"));
        if (!optional.get().getLevelName().equals(memberLevel.getLevelName())) {
            MemberLevel oldMemberLevel = memberLevelRepository.findByLevelName(memberLevel.getLevelName());
            if (oldMemberLevel != null && !oldMemberLevel.getId().equals(memberLevel.getId())) {
                throw new Exception("该会员等级名称已存在");
            }
        }
        MemberLevel oldMemberLevel = optional.get();
        memberLevel.setDeletedFlag(oldMemberLevel.getDeletedFlag());
        memberLevel.setUpdatedTime(new Date());
        return memberLevelRepository.saveAndFlush(memberLevel);
    }

    public void delete(Long id) {
        memberLevelRepository.deleteById(id);
    }

    public void logicDelete(Long id) {
        boolean exists = memberLevelRepository.existsById(id);
        if (exists) {
            memberLevelRepository.updateDeletedFlag(true, id);
        }
    }

    public MemberLevel getById(Long id) {
        return memberLevelRepository.getById(id);
    }

    public List<MemberLevel> listAll() {
        return memberLevelRepository.findAll();
    }

    public List<MemberLevel> listAllEnabled() {
        return memberLevelRepository.findByDeletedFlag(false);
    }

}
