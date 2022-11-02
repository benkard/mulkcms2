import '../web_modules/ContentTools/build/content-tools.min.js';

window.addEventListener('DOMContentLoaded', () => {
  let editor = ContentTools.EditorApp.get();
  editor.init('*[data-editable]', 'data-name');

  let {pageTitle} = document.getElementById('wiki-page').dataset;

  editor.addEventListener('saved', async function (ev) {
    document.getElementById("warning-panel").close();

    let {regions} = ev.detail();
    if (Object.getOwnPropertyNames(regions).length === 0) {
      // Nothing changed.
      return;
    }

    this.busy(true);

    let requestParams = new URLSearchParams();
    for (let name of Object.getOwnPropertyNames(regions)) {
      requestParams.append(name, regions[name]);
    }

    let response = await fetch(`/wiki/${pageTitle}`, {
      method: 'POST',
      body: requestParams
    });

    if (!response.ok) {
      document.getElementById("warning-panel").open();
      document.getElementById("warning-text").innerText = `Failed to save page: ${response.statusText}`;
      this.busy(false);

      return;
    }

    let status = await response.json();
    if (status.status !== "ok") {
      document.getElementById("warning-panel").open();
      document.getElementById("warning-text").innerText = `Failed to save page: ${JSON.stringify(status)}`;
      this.busy(false);

      return;
    }

    if (status.hasOwnProperty("content")) {
      document.getElementById("wiki-content").innerHTML = status.content;
    }

    if (status.hasOwnProperty("title")) {
      pageTitle = status.title;
    }

    this.busy(false);
  });
});
