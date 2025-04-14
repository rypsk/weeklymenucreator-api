package com.rypsk.weeklymenucreator.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Attachment {
    private String fileName;
    private String contentType;
    private byte[] content;
}
