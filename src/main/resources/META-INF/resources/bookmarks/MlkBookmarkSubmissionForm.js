import {html, render} from "../../web_modules/lit-html.js";
import ProgressSpinner from "../web_modules/elix/define/ProgressSpinner.js";

export class MlkBookmarkSubmissionForm extends HTMLElement {
  constructor() {
    super();

    this.onUriBlur = this.onUriBlur.bind(this);

    this.attachShadow({mode: "open"});
  }

  static get observedAttributes() {
    return ["greetee"];
  }

  connectedCallback () {
    this.render();
  }

  attributeChangedCallback(name, oldValue, newValue) {
    this.render();
  }

  get uri() {
    return this.getAttribute("uri");
  }

  get title() {
    return this.getAttribute("title");
  }

  get description() {
    return this.getAttribute("description");
  }

  focus() {
    let uriInput = this.shadowRoot.getElementById('uri-input');
    let titleInput = this.shadowRoot.getElementById('title-input');
    let descriptionInput = this.shadowRoot.getElementById('description-input');

    if (!uriInput.value) {
      uriInput.focus();
    } else if (!titleInput.value) {
      titleInput.focus();
      this.onUriBlur();
    } else {
      descriptionInput.focus();
    }
  }

  async onUriBlur() {
    let titleInput = this.shadowRoot.getElementById('title-input');
    let uriInput = this.shadowRoot.getElementById('uri-input');
    let uriSpinner = this.shadowRoot.getElementById('uri-spinner');

    if (!uriInput.value) {
      return;
    }

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
  }

  render() {
    const template = html`
      <link rel="stylesheet" type="text/css" href="/cms2/base.css" />
      <link rel="stylesheet" type="text/css" href="/bookmarks/MlkBookmarkSubmissionForm.css" />

      <form class="pure-form" method="post" action="/bookmarks">
        <fieldset>
          <legend>New Bookmark</legend>

          <label for="uri-input">URI:</label>
          <input name="uri" id="uri-input" type="text" placeholder="URI" required
                 value=${this.uri || ""}
                 @blur=${this.onUriBlur} />
          <elix-progress-spinner id="uri-spinner" hidden></elix-progress-spinner>

          <label for="title-input">Title:</label>
          <input name="title" id="title-input" type="text" placeholder="Title" required
                 value="${this.title || ""}" />

          <label for="description-input">Description:</label>
          <textarea name="description" id="description-input" placeholder="Description"
              >${this.description || ""}</textarea>

          <label for="visibility-input">Visibility:</label>
          <select id="visibility-input" name="visibility" required>
            <option value="public" selected>Public</option>
            <option value="semiprivate">Semiprivate</option>
            <option value="private">Private</option>
          </select>

          <div class="controls">
            <button type="submit" class="pure-button pure-button-primary">Submit Bookmark</button>
          </div>
        </fieldset>
      </form>`;

    render(template, this.shadowRoot);
  }
}

customElements.define("mlk-bookmark-submission-form", MlkBookmarkSubmissionForm);
