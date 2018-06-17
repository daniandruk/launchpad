package org.daniandruk.launchpad;

//Your username is: 
//
//j3845180 
//
//Visit this link to verify your email address: 
//
//https://www.reddit.com/verification/4vzCgG9g2tI0nHdgbc8i2C5iK-s?ref_campaign=verify_email&ref_source=email&ref=verify_email 
//
//Please do not reply to this notification, this inbox is not monitored. 
//
//If you are having a problem with your account, please email contact@reddit.com 
//
//Thanks for using the site! 
// PHnAYBIIpt89pw
// 9snKYckOwEA1TO9ZIhPA5KorKHs
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
