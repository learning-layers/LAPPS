package de.rwth.dbis.layers.lapps.domain;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.Comment;
import de.rwth.dbis.layers.lapps.entity.User;

public class CommentTest {
  private static Facade facade = new Facade();
  private List<App> apps = null;
  private List<User> users = null;
  private Comment comment = null;

  @Before
  public void save() {
    if (this.apps == null) {
      this.apps = facade.loadAll(App.class);
      Assert.assertNotNull("Apps list is null!", apps);
      Assert.assertTrue("Apps list is empty!", apps.size() > 0);
    }
    if (this.users == null) {
      this.users = facade.loadAll(User.class);
      Assert.assertNotNull("Users list is null!", users);
      Assert.assertTrue("Users list is empty!", users.size() > 0);
    }
    Comment comment = new Comment("Test", this.users.get(0), this.apps.get(0));
    this.comment = facade.save(comment);
    Assert.assertTrue("Comment appears not to be saved!", this.comment.getId() > 0);
  }

  @After
  public void delete() {
    facade.deleteByParam(Comment.class, "id", this.comment.getId());
    List<Comment> ifDeleted = facade.findByParam(Comment.class, "id", this.comment.getId());
    Assert.assertTrue("Comment apperantly not deleted!", ifDeleted.size() == 0);
  }

  @Test
  public void load() {
    List<Comment> comments = facade.findByParam(Comment.class, "id", this.comment.getId());
    Assert.assertFalse("There are multiple elements in the list when searching by id!",
        comments.size() > 1);
    Assert.assertEquals("The IDs of the comment saved and comment load missmatch!",
        this.comment.getId(), comments.get(0).getId());
  }

}
