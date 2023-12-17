package com.catcher.common.exception;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(200, "요청에 성공하였습니다."),
    DATA_NOT_FOUND(1001, "해당 데이터를 찾을 수 없습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    NO_ADDRESS_RESULT_FOR_QUERY(2000, "해당하는 요청에 대응되는 주소 검색 결과가 없습니다."),
    NO_LOCATION_RESULT(2001, "해당하는 요청에 대응되는 법정동 코드가 없습니다"),
    INVALID_JWT(2002, "토큰 정보가 유효하지 않습니다."),
    NO_ACCESS_AUTHORIZATION(2003, "접근 권한이 없습니다."),
    NO_ITEM_TYPE_RESULT(2001,"해당 아이템 타입에 대한 결과가 없습니다"),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(3000, "값을 불러오는데 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(4000, "데이터베이스 연결에 실패하였습니다."),
    REDIS_ERROR(4002, "redis 연결에 실패하였습니다."),
    FAIL_DELETE_DRAFT_SCHEDULE(4003, "작성 중인 일정을 삭제하는 데 실패하였습니다."),
    /**
     * 5000: AWS Error
     */
    S3UPLOAD_ERROR(5000, "파일 업로드를 실패하였습니다."),
    KMS_ERROR(5001, "암호화 및 복호화 과정에서 실패하였습니다."),
    AWS_IO_ERROR(5002, "파일의 정보를 가져오는 데 실패했습니다.")
    ;

    private final int code;
    private final String message;

    BaseResponseStatus(int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.code = code;
        this.message = message;
    }

    public static BaseResponseStatus of(final String errorName){
        return BaseResponseStatus.valueOf(errorName);
    }
}
