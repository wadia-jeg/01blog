package com.zone._blog.moderation.dto;

public record PostByPeriod(
        Long count,
        Integer period,
        Integer year
        ) {

}
