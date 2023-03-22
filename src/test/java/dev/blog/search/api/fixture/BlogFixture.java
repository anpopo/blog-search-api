package dev.blog.search.api.fixture;

import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse.Document;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse.Meta;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;

public class BlogFixture {

    public static Set<TypedTuple<String>> getTypedTupleFixture() {
        Set<TypedTuple<String>> typedTupleSet = new TreeSet<>(
            (o1, o2) -> o2.getScore().compareTo(o1.getScore())
        );

        typedTupleSet.add(new DefaultTypedTuple<>("test3", 3.0));
        typedTupleSet.add(new DefaultTypedTuple<>("test2", 2.0));
        typedTupleSet.add(new DefaultTypedTuple<>("test1", 1.0));

        return typedTupleSet;
    }

    public static Meta getMetaFixture() {
        Meta meta = new Meta();
        meta.setEnd(false);
        meta.setPageableCount(10);
        meta.setTotalCount(100);

        return meta;
    }

    public static List<Document> getDocumentListFixture() {
        List<Document> documentList = new ArrayList<>();
        documentList.add(getDocumentFixture());
        documentList.add(getDocumentFixture());
        documentList.add(getDocumentFixture());

        return documentList;
    }

    private static Document getDocumentFixture() {
        Document document = new Document();
        document.setTitle("test title");
        document.setBlogName("test blog");
        document.setContents("test contents");
        document.setDatetime("2023-01-01T00:00:00.000");
        document.setThumbnail("test thumbnail");
        document.setUrl("test url");

        return document;
    }

}
