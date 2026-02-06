package com.zone._blog.exceptions;

import java.util.Map;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle DataIntegrityViolationException
    // Not Found Errors
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                exception.getMessage(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Bad Resquest Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(err -> Map.of("field", err.getField(), "message", err.getDefaultMessage()))
                        .toList(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getConstraintViolations()
                        .stream()
                        .map(err -> Map.of("feild", err.getPropertyPath(), "message", err.getMessage()))
                        .toList(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // AuthenticationErrors
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationExcpetion(AuthenticationException exception) {

        ErrorResponse errorResponse = new ErrorResponse(
                "User not found",
                HttpStatus.UNAUTHORIZED.value(),
                exception.getExplanation(),
                exception.getCause()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized Access",
                HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage(),
                exception.getCause()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> BadCredentialsExcpetion(BadCredentialsException exception) {
        Authentication request = exception.getAuthenticationRequest();
        Object credentials = request.getCredentials();

        System.out.println("credentials for bad " + credentials.toString());
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid credentials",
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getCause()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundExcpetion(UsernameNotFoundException exception) {
        Authentication request = exception.getAuthenticationRequest();
        Object credentials = request.getCredentials();

        System.out.println("credentials for not found" + credentials.toString());
        ErrorResponse errorResponse = new ErrorResponse(
                "User not found",
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                exception.getCause()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Internal Server Errors
    @ExceptionHandler(Exception.class)

    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Sorry, something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                exception.getCause()
        );

        System.out.println(errorResponse.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}


/*
When building a robust Spring application, especially one dealing with databases, web requests, and authentication, you'll want to handle a variety of exceptions to provide useful feedback to your users and ensure smooth operation. Here's a list of **common exceptions** that you should handle in a typical Spring application, categorized by the type of issue:

### 1. **Database-Related Exceptions**

These exceptions are related to database operations (e.g., constraints, data integrity, etc.).

* **`DataIntegrityViolationException`**

  * Thrown when a database constraint is violated, such as a unique constraint violation, foreign key violation, etc.
  * Example: Trying to insert a duplicate entry in a column marked as `unique`.

* **`ConstraintViolationException`**

  * Thrown when a constraint violation happens (often from JPA/Hibernate).
  * You may encounter this if an entity violates a constraint (like `NotNull` or `Size` constraints).

* **`EntityNotFoundException`**

  * Thrown when a JPA query fails to find the entity (e.g., using `getOne()` or `getById()`).
  * Example: Trying to fetch a user by an ID that doesn't exist.

* **`OptimisticLockException`**

  * Thrown when an optimistic lock fails (e.g., when another transaction modified the entity you're trying to save).

* **`QueryTimeoutException`**

  * Thrown when a database query exceeds the configured timeout period.

### 2. **Web and HTTP-Related Exceptions**

These exceptions are related to HTTP requests, validation, or method handling.

* **`HttpRequestMethodNotSupportedException`**

  * Thrown when an HTTP request method (GET, POST, PUT, DELETE) is not supported for a given endpoint.

* **`MethodArgumentNotValidException`**

  * Thrown when a method parameter fails validation (e.g., for `@Valid` or `@RequestBody`).

* **`ConstraintViolationException`** (validation)

  * Used with **JSR 303/JSR 380** constraints, such as `@NotNull`, `@Email`, etc. It may be triggered if the validation annotations on a DTO or entity field fail.

* **`MissingServletRequestParameterException`**

  * Thrown when a required parameter in the request is missing.

* **`HttpMessageNotReadableException`**

  * Thrown when the request body cannot be deserialized into the desired object (e.g., invalid JSON).

* **`HttpMessageNotWritableException`**

  * Thrown when the response body cannot be serialized into the response format (e.g., JSON or XML).

* **`HttpMediaTypeNotSupportedException`**

  * Thrown when the requested media type (content type) is not supported by the server.

* **`NoHandlerFoundException`**

  * Thrown when there is no handler for a given URL (like a 404 error).

### 3. **Security and Authentication-Related Exceptions**

These exceptions are related to security, authorization, and authentication.

* **`AuthenticationException`**

  * A generic exception thrown when an authentication error occurs (e.g., invalid credentials during login).

* **`BadCredentialsException`**

  * Thrown when credentials are incorrect (e.g., invalid username/password combination).

* **`UsernameNotFoundException`**

  * Thrown when a username is not found in the system during authentication.

* **`AccessDeniedException`**

  * Thrown when a user tries to access a resource they don't have permission to view.

* **`UnauthorizedException`**

  * Thrown when a user is not authorized to perform a particular action.

* **`ExpiredJwtException`**

  * Thrown when a JWT (JSON Web Token) has expired.

* **`InvalidTokenException`**

  * Thrown when a token is invalid or malformed.

### 4. **General Runtime and System Exceptions**

These are general exceptions that can occur anywhere in the application and usually require proper handling to prevent the application from crashing.

* **`ResourceNotFoundException`**

  * A custom exception to handle scenarios where a resource is not found (e.g., a user or post that doesn't exist).

* **`BadRequestException`**

  * A custom exception for situations where the user sends bad data (invalid input or parameters).

* **`ConflictException`**

  * Thrown when there is a conflict in the state of a resource, like trying to update a resource that is already being modified.

* **`IllegalArgumentException`**

  * Thrown when a method is passed an illegal or inappropriate argument.

* **`NullPointerException`**

  * Thrown when attempting to use a `null` value where an object is required (though this should ideally be avoided through proper validation).

* **`RuntimeException`**

  * This is a catch-all for unexpected runtime errors.

* **`Exception`**

  * A general exception that is used to catch unforeseen issues (use as a fallback in the `GlobalExceptionHandler`).

### 5. **Specific Spring and Hibernate Exceptions**

These exceptions are related to Spring's internal workings and persistence mechanisms.

* **`TransactionSystemException`**

  * Thrown when a transaction system fails, such as when a rollback fails.

* **`PersistenceException`**

  * A general exception thrown by the persistence layer, which may have different causes (like an issue with the database connection).

* **`OptimisticLockingFailureException`**

  * Thrown when optimistic locking (e.g., `@Version` annotations) fails.

* **`JDBCException`**

  * A specific exception related to JDBC issues such as connection problems, SQL errors, etc.

* **`JdbcSQLSyntaxErrorException`**

  * Thrown when there is a syntax error in the SQL query.

### 6. **Validation and Custom Exceptions**

You can also create custom exceptions to handle specific business logic.

* **`ValidationException`**

  * A custom exception to handle failed validation logic.

* **`BadRequestException`**

  * Custom exception when the request sent to the server is bad, e.g., incorrect data format.

### Handling These Exceptions in `@RestControllerAdvice`

You can globally handle these exceptions in your `@RestControllerAdvice` class (as you've done with `ResourceNotFoundException`, `BadRequestException`, etc.).

For instance:

```java
@ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
            "Database error: integrity violation",
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            ex.getCause()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(UsernameNotFoundException.class)
public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
            "User not found",
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            ex.getCause()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
                             .getFieldErrors()
                             .stream()
                             .map(error -> error.getDefaultMessage())
                             .collect(Collectors.toList());

    ErrorResponse errorResponse = new ErrorResponse(
            "Validation failed",
            HttpStatus.BAD_REQUEST.value(),
            errors,
            ex.getCause()
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}
```

### Conclusion: Which Exceptions Should You Handle?

* **For common issues like invalid input, user authentication errors, and not found errors**, you'll want to handle things like `BadRequestException`, `UsernameNotFoundException`, and `ResourceNotFoundException`.

* **For database integrity issues**, you should definitely handle `DataIntegrityViolationException` and `ConstraintViolationException`.

* **For any general issues** (unexpected issues), it is good practice to have a fallback handler for `Exception.class`.

With global exception handling in place (like `@RestControllerAdvice`), you won't need to handle these exceptions explicitly in each service method, as Spring will handle them centrally.


 */
