package com.catcher.common.exception;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    NO_ADDRESS_RESULT_FOR_QUERY(false, 2000, "해당하는 요청에 대응되는 주소 검색 결과가 없습니다."),
    NO_LOCATION_RESULT(false, 2001, "해당하는 요청에 대응되는 법정동 코드가 없습니다"),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    REDIS_ERROR(false, 4002, "redis 연결에 실패하였습니다."),

    /**
     * 5000: AWS Error
     */
    S3UPLOAD_ERROR(false, 5000, "파일 업로드를 실패하였습니다."),
    KMS_ERROR(false, 5001, "암호화 및 복호화 과정에서 실패하였습니다."),
    AWS_IO_ERROR(false, 5002, "파일의 정보를 가져오는 데 실패했습니다.")
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    public static BaseResponseStatus of(final String errorName){
        return BaseResponseStatus.valueOf(errorName);
    }
}
