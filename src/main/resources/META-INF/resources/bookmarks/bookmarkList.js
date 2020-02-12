document.addEventListener('DOMContentLoaded', () => {
  let bookmarkSubmissionPane = document.getElementById('bookmark-submission-pane');
  let uriInput = document.getElementById('uri-input');
  bookmarkSubmissionPane.addEventListener('opened', () => uriInput.focus());
});