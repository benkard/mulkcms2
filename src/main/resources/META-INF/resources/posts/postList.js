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

  let lazychatEditorPanes = document.getElementsByClassName('lazychat-editor-pane');
  for (let pane of lazychatEditorPanes) {
    let form = pane.getElementsByTagName('mlk-lazychat-submission-form')[0];
    pane.addEventListener('opened', () => form.focus());
  }
});
