package com.devsuperior.dsclient.controller.exceptions;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class StandardError implements Serializable {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}
