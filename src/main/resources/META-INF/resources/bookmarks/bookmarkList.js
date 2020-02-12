document.addEventListener('DOMContentLoaded', () => {
  let bookmarkSubmissionPane = document.getElementById(
      'bookmark-submission-pane');
  let titleInput = document.getElementById('title-input');
  let uriInput = document.getElementById('uri-input');
  let uriSpinner = document.getElementById('uri-spinner');

  bookmarkSubmissionPane.addEventListener('opened', () => uriInput.focus());

  uriInput.addEventListener('blur', async () => {
    uriSpinner.hidden = false;
    uriSpinner.playing = true;
    let searchParams = new URLSearchParams({'uri': uriInput.value});
    console.log(`/bookmarks/page-info?${searchParams}`);
    let fetchUrl = new URL(`/bookmarks/page-info?${searchParams}`, document.URL);
    let r = await fetch(fetchUrl);
    uriSpinner.hidden = true;
    uriSpinner.playing = false;

    if (!r.ok) {
      return;
    }

    let pageInfo = await r.json();
    titleInput.value = pageInfo.title;
  });
});

