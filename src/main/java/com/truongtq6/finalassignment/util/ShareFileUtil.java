package com.truongtq6.finalassignment.util;

import com.truongtq6.finalassignment.constant.Permission;

public class ShareFileUtil {

    public static boolean canRead(Permission permission) {
        return Permission.READ.equals(permission) || Permission.SHARE.equals(permission) || Permission.OWNER.equals(permission);
    }

    public static boolean canShare(Permission permission) {
        return Permission.SHARE.equals(permission) || Permission.OWNER.equals(permission);
    }

    public static boolean isOwner(Permission permission) {
        return Permission.OWNER.equals(permission);
    }

}
