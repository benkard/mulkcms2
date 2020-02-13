document.addEventListener('DOMContentLoaded', () => {
  let bookmarkSubmissionPane = document.getElementById(
      'bookmark-submission-pane');
  let bookmarkSubmissionForm = document.getElementById(
      'bookmark-submission-form');

  bookmarkSubmissionPane.addEventListener('opened', () => bookmarkSubmissionForm.focus());
});

