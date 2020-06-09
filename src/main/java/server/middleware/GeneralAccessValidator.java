package server.middleware;

import domain.user.User;
import domain.userRepository.UserRepository;
import domain.userRepository.exception.UserRepositoryException;
import domain.userRepository.getQuery.GetQuery;
import domain.userRepository.getQuery.GetQueryWithSpecialField;
import manager.LogManager;
import middleware.Middleware;
import middleware.MiddlewareException;
import query.Query;
import response.Response;
import response.ResponseDTO;
import response.Status;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.NoSuchElementException;


public final class GeneralAccessValidator extends Middleware {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(GeneralAccessValidator.class);

    private final UserRepository userRepository;


    private String nextLeave = "old";


    public GeneralAccessValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response handle(@Nonnull Query query) throws MiddlewareException {
        String login = query.getArguments().get("login");

        GetQuery getQuery = new GetQueryWithSpecialField(User.class, "login", login);

        User user;
        try {
            List<User> users = userRepository.get(getQuery);

            if (users.isEmpty()) {
                ResponseDTO responseDTO = new ResponseDTO();
                responseDTO.status = Status.BAD_REQUEST.getStringResult();
                responseDTO.answer = "No such user. Probably was deleted.";
                return Response.createResponse(responseDTO);
            }

            user = userRepository.get(getQuery).iterator().next();
        } catch (NoSuchElementException | UserRepositoryException e) {
            LOG_MANAGER.errorThrowable(e);
            return Response.createInternalError();
        }

        query.getArguments().remove("login");
        query.getArguments().remove("password");
        query.getArguments().put("userId", String.valueOf(user.getId()));

        return callLeave(query, nextLeave);
    }

    @Override
    public Response handle(@Nonnull Query query, @Nonnull String targetLeaveKey) throws MiddlewareException {
        nextLeave = targetLeaveKey;
        return handle(query);
    }
}
