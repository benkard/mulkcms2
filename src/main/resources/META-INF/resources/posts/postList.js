document.addEventListener('DOMContentLoaded', () => {
  let bookmarkSubmissionPane = document.getElementById('bookmark-submission-pane');
  if (bookmarkSubmissionPane) {
    let bookmarkSubmissionForm = document.getElementById('bookmark-submission-form');
    bookmarkSubmissionPane.addEventListener('opened',() => bookmarkSubmissionForm.focus());
  }

  let lazychatSubmissionPane = document.getElementById('lazychat-submission-pane');
  if (lazychatSubmissionPane) {
    let lazychatSubmissionForm = document.getElementById('lazychat-submission-form');
    lazychatSubmissionPane.addEventListener('opened',() => lazychatSubmissionForm.focus());
  }

  let lazychatMessages = document.getElementsByClassName('lazychat-message');
  for (let message of lazychatMessages) {
    let editorPane = message.getElementsByClassName('lazychat-editor-pane')[0];
    if (editorPane) {
      let form = message.getElementsByTagName('mlk-lazychat-submission-form')[0];
      let editButton = message.getElementsByClassName('lazychat-edit-button')[0];
      editButton.addEventListener('click', () => {
        editorPane.toggle();
        form.focus();
      });
    }
  }

  let bookmarks = document.getElementsByClassName('bookmark');
  for (let bookmark of bookmarks) {
    let editorPane = bookmark.getElementsByClassName('editor-pane')[0];
    if (editorPane) {
      let form = bookmark.getElementsByTagName('mlk-bookmark-submission-form')[0];
      let editButton = bookmark.getElementsByClassName('bookmark-edit-button')[0];
      editButton.addEventListener('click', () => {
        editorPane.toggle();
        form.focus();
      });
    }
  }
});
