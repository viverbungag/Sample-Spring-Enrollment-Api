package com.skipp.enlistment.dto;

/**
 * Object to be returned by the API in case of an error.
 *
 * @param message   meaningful message to describe the error that happened
 */
public record ErrorResponse (String message) {
}
