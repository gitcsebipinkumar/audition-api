package com.audition.web.advice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.audition.common.exception.PostNotFoundException;
import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    public static final String DEFAULT_TITLE = "API Error Occurred";
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";

    @Autowired
    private transient AuditionLogger auditionLogger;

    public ExceptionControllerAdvice(AuditionLogger logger) {
        super();
        this.auditionLogger = logger;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    ProblemDetail handleHttpClientException(final HttpClientErrorException e) {
        return createProblemDetail(e, e.getStatusCode());

    }

    /**
     * Handles exceptions of type {@link PostNotFoundException}.
     *
     * <p>
     * This method is invoked when a {@code PostNotFoundException} is thrown within the application. It constructs a
     * {@link ProblemDetail} with the appropriate error status and detailed message extracted from the exception, and
     * returns it wrapped in a {@link ResponseEntity}.
     * </p>
     *
     * @param e The {@link PostNotFoundException} instance thrown.
     * @return A {@link ResponseEntity} containing a {@link ProblemDetail} representing the error response.
     */

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePostNotFoundException(final PostNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND; // HTTP 404 Not Found
        ProblemDetail problemDetail = createProblemDetail(e, status);
        problemDetail.setDetail(e.getMessage()); // Set detailed error message from the exception
        return ResponseEntity.status(status).body(problemDetail);
    }

    /**
     * Handles exceptions of type {@link SystemException}.
     *
     * <p>
     * This method is invoked when a {@code SystemException} is thrown within the application. It determines the
     * appropriate HTTP status based on the exception, constructs a {@link ProblemDetail} with the detailed message
     * extracted from the exception, and returns it wrapped in a {@link ResponseEntity}.
     * </p>
     *
     * @param e The {@link SystemException} instance thrown.
     * @return A {@link ResponseEntity} containing a {@link ProblemDetail} representing the error response.
     */

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ProblemDetail> handleSystemException(final SystemException e) {
        HttpStatus status = (HttpStatus) getHttpStatusCodeFromSystemException(e);
        return ResponseEntity.status(status)
            .body(createProblemDetail(e, status));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleMainException(final Exception e) {
        HttpStatus status = (HttpStatus) getHttpStatusCodeFromException(e);
        return ResponseEntity.status(status)
            .body(createProblemDetail(e, status));
    }


    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        if (exception instanceof SystemException) {
            problemDetail.setTitle(((SystemException) exception).getTitle());
        } else {
            problemDetail.setTitle(DEFAULT_TITLE);
        }
        return problemDetail;
    }

    private String getMessageFromException(final Exception exception) {
        if (StringUtils.isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    private HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        try {
            return HttpStatusCode.valueOf(exception.getStatusCode());
        } catch (final IllegalArgumentException iae) {
            auditionLogger.info(LOG, ERROR_MESSAGE + exception.getStatusCode());
            return INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) exception).getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return METHOD_NOT_ALLOWED;
        }
        return INTERNAL_SERVER_ERROR;
    }
}



