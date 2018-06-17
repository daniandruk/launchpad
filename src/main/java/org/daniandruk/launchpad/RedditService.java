package org.daniandruk.launchpad;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.SearchSort;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.SearchPaginator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class RedditService {

    private final RedditClient redditClient;

    @Inject
    public RedditService(@Value("${reddit.username}") String username, @Value("${reddit.password}") String password,
            @Value("${reddit.clientid}") String clientid, @Value("${reddit.secret}") String secret) {
        UserAgent userAgent = new UserAgent("Launchpad");
        Credentials credentials = Credentials.script(username, password, clientid, secret);
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        redditClient = OAuthHelper.automatic(adapter, credentials);
    }

    public List<Item> getItems(String query, int limit) {
        SearchPaginator searchPaginator = redditClient.subreddits("pics", "videos").search().query(query).sorting(SearchSort.COMMENTS).limit(limit).build();
        List<Submission> submissions = searchPaginator.accumulateMerged(1);
        List<Item> items = new ArrayList<>();
        submissions.stream().map((submission) -> {
            Item item = new Item();
            item.setName(submission.getFullName());
            item.setUrl(submission.getUrl());
            item.setCount(submission.getCommentCount());
            return item;
        }).forEach((item) -> {
            items.add(item);
        });
        return items;
    }

}
