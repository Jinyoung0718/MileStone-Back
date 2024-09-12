package Milestone.spring_project.backend.Exception;

public class ProductOptionNotFoundException extends RuntimeException {
    public ProductOptionNotFoundException(String message) {
        super(message);
    }
}
