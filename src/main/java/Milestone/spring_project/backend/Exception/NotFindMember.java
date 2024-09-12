package Milestone.spring_project.backend.Exception;

public class NotFindMember extends  RuntimeException {
    public NotFindMember (String message) {
        super(message);
    }
}