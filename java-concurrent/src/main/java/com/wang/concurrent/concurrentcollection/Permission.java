package com.wang.concurrent.concurrentcollection;

/**
 * 通过 权限控制，来说明 位运算
 *
 * @author weiman cui
 * @date 2020/5/17 17:20
 */
public class Permission {

    /**
     * 是否允许查询，校验二进制第1位，0否，1是
     */
    public static final int ALLOW_SELECT = 1 << 0;

    /**
     * 是否允许新增，校验二进制第2位，0否，1是
     */
    public static final int ALLOW_INSERT = 1 << 1;

    /**
     * 是否允许修改，校验二进制第3位，0否，1是
     */
    public static final int ALLOW_UPDATE = 1 << 2;

    /**
     * 是否允许删除，校验二进制第4位，0否，1是
     */
    public static final int ALLOW_DELETE = 1 << 4;

    /**
     * 存储目前的权限状态
     */
    private int flag;

    /**
     * 设置用户权限
     *
     * @param per
     */
    public void setPer(int per) {
        flag = per;
    }

    /**
     * 增加用户的权限，1个或多个
     *
     * @param per
     */
    public void enable(int per) {
        flag = flag | per;
    }

    /**
     * 删除用户的权限,与上 一个非
     *
     * @param per
     */
    public void disable(int per) {
        flag = flag & (~per);
    }

    /**
     * 判定用户是否拥有 权限
     *
     * @param per
     * @return
     */
    public boolean isAllow(int per) {
        return (flag & per) == per;
    }

    /**
     * 判定用户 是否 没有权限
     *
     * @param per
     * @return
     */
    public boolean isNotAllow(int per) {
        return (flag & per) == 0;
    }

    public static void main(String[] args) {
        int flag = 15;
        Permission permission = new Permission();
        permission.setPer(flag);

        permission.disable(ALLOW_DELETE | ALLOW_INSERT);
        System.out.println("select = " + permission.isAllow(ALLOW_SELECT));
        System.out.println("insert = " + permission.isAllow(ALLOW_INSERT));
        System.out.println("update = " + permission.isAllow(ALLOW_UPDATE));
        System.out.println("delete = " + permission.isAllow(ALLOW_DELETE));
    }

}
