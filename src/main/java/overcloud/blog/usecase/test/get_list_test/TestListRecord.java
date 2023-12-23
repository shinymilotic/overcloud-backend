package overcloud.blog.usecase.test.get_list_test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TestListRecord {
    private String title;
    private String slug;
}