package com.example.ms.service;

import com.example.ms.common.constant.UserConst;
import com.example.ms.model.bo.Role;
import com.example.ms.model.bo.User;
import com.example.ms.model.dto.SearchParam;
import com.example.ms.model.dto.TreeNode;
import com.example.ms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public Page<User> search(SearchParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize(), Sort.Direction.ASC, "id");
        String keyword = param.getKeyword();
        if (keyword == null || "".equals(keyword)) {
            return userRepository.findByDeletedFlag(false, pageable);
        }
        return userRepository.findByDeletedFlagAndUsernameLike(false, "%" + keyword + "%", pageable);
    }

    public User create(User user) throws Exception {
        User oldUser = userRepository.getByUsername(user.getUsername());
        if (oldUser != null && !oldUser.getDeletedFlag()) {
            throw new Exception("记录已存在");
        }
        if (oldUser != null && oldUser.getDeletedFlag()) {
            user.setId(oldUser.getId());
        }
        user.setDeletedFlag(false);
        user.setCreatedTime(new Date());
        return userRepository.saveAndFlush(user);
    }

    public User update(User user) throws Exception {
        if (user.getId() == null || user.getId() <= 0) {
            throw new Exception("更新信息异常");
        }
        Optional<User> optional = userRepository.findById(user.getId());
        optional.orElseThrow(() -> new Exception("找不到该记录"));
        if (!optional.get().getUsername().equals(user.getUsername())) {
            User oldUser = userRepository.getByUsername(user.getUsername());
            if (oldUser != null && !oldUser.getId().equals(user.getId())) {
                throw new Exception("记录已存在");
            }
        }
        User oldUser = optional.get();
        user.setDeletedFlag(oldUser.getDeletedFlag());
        user.setCreatedTime(oldUser.getCreatedTime());
        user.setUpdatedTime(new Date());
        return userRepository.saveAndFlush(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void logicDelete(Long id) {
        boolean exists = userRepository.existsById(id);
        if (exists) {
            userRepository.updateDeletedFlag(true, id);
        }
    }

    public void increaseFailedCount(User user) {
        int failCount = user.getFailedCount() + 1;
        userRepository.updateFailedCount(failCount, user.getId());
    }

    public void resetFailedCount(Long userId) {
        userRepository.updateFailedCount(UserConst.DEFAULT_FAILED_COUNT, userId);
    }

    public void lockAccount(User user) {
        user.setUnlockFlag(false);
        user.setLockTime(new Date());

        userRepository.save(user);
    }

    public boolean unlockWhenLockExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + UserConst.LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setUnlockFlag(true);
            user.setLockTime(null);
            user.setFailedCount(UserConst.DEFAULT_FAILED_COUNT);

            userRepository.save(user);
            return true;
        }

        return false;
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /*
     * {
     *    text:"P1",
     *    nodeid:"1",
     *    nodes: [
     *        {
     *           text:"C1",
     *           nodeid:"11"
     *        },
     *        {
     *           text:"C2",
     *           nodeid:"12"
     *        },
     *        {...}
     *    ]
     * }
     * */
    public List<TreeNode> userRoleTreeNodes(Long id) {
        List<TreeNode> treeNodeList = new ArrayList<>();

        User user = userRepository.getById(id);
        Set<Role> userRoleList = user.getRoles();
        List<Long> userRoleIdList = userRoleList.stream().map(Role::getId).collect(Collectors.toList());
        List<Role> roleList = roleService.listAll();
        if (roleList.isEmpty()) {
            return treeNodeList;
        }
        for (Role role : roleList) {
            TreeNode treeNode = new TreeNode(role.getId(), role.getRoleName());
            boolean setFlag = userRoleIdList.contains(role.getId());
            treeNode.setState(setFlag, role.getDeletedFlag(), setFlag);

            treeNodeList.add(treeNode);
        }

        return treeNodeList;
    }

    @Transactional
    public void userRolesAssign(Long id, List<Long> roleIds) {
        User user = userRepository.getById(id);
        user.setRoles(new HashSet<>());
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                user.getRoles().add(roleService.getById(roleId));
            }
        }
        userRepository.save(user);
    }

}
