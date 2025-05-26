package com.eps.shared.models.exceptions;

public enum CommonErrorMessage implements BaseErrorMessage {
    FORBIDDEN("Bạn không có quyền truy cập"),
    VALIDATION_FAILED("Xác minh dữ liệu thất bại"),
    FIELD_CANT_SORT("Trường #fieldname# không thể sắp xếp"),
    INTERNAL_SERVER("Hệ thống có lỗi xảy ra xin vui lòng thử lại sau"),
    ENUM_FAILED("Không thể chuyển đổi #value# thành loại #type#"),
    NOT_FOUND("Không tìm thấy dữ liệu");

    CommonErrorMessage(String message) {
        val = message;
    }

    private final String val;

    @Override
    public String val() {
        return val;
    }
}
