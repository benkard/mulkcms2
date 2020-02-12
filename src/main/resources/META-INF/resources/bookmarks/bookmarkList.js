document.addEventListener('DOMContentLoaded', () => {
  let bookmarkSubmissionPane = document.getElementById('bookmark-submission-pane');
  let titleInput = document.getElementById('title-input');
  let uriInput = document.getElementById('uri-input');
  let uriSpinner = document.getElementById('uri-spinner');

  bookmarkSubmissionPane.addEventListener('opened', () => uriInput.focus());

  uriInput.addEventListener('blur', async () => {
    uriSpinner.hidden = false;
    uriSpinner.playing = true;
    let r = await fetch(uriInput.value);
    uriSpinner.hidden = true;
    uriSpinner.playing = false;

    if (!r.ok) {
      return;
    }

    let html = await r.text();
    let parser = new DOMParser();
    let doc = parser.parseFromString(html, "text/html");
    let titles = doc.getElementsByTagName('title');
    if (titles.length <= 0) {
      return;
    }
    titleInput.value = titles[0].innerText;
  });
});

