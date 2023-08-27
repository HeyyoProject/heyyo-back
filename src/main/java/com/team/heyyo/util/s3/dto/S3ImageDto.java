package com.team.heyyo.util.s3.dto;

public record S3ImageDto(
        String originalFileName,
        String serverFileName
) {
    public static S3ImageDto of(String originalFileName, String serverFileName) {
        return new S3ImageDto(originalFileName, serverFileName);
    }
}
