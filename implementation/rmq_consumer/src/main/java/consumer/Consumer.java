package consumer;

import util.SkierServletPostRequest;

/**
 * Represent the logic to consume the request from user.
 */
public interface Consumer {

    /**
     * Consumes the user request given the request entity.
     *
     * @param request the body of the request.
     */
    void consume(SkierServletPostRequest request);
}
