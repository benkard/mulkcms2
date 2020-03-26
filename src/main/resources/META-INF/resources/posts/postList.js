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
});
