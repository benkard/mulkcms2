import {html, render} from "../../web_modules/lit-html.js";
import ProgressSpinner from "../web_modules/elix/define/ProgressSpinner.js";

export class MlkBookmarkSubmissionForm extends HTMLElement {
  constructor() {
    super();

    this.onUriBlur = this.onUriBlur.bind(this);

    this.attachShadow({mode: "open"});
  }

  static get observedAttributes() {
    return [];
  }

  connectedCallback () {
    this.render();

    let shadow = this.shadowRoot;
    this.descriptionInput = shadow.getElementById('description-input');
    this.titleInput = shadow.getElementById('title-input');
    this.uriInput = shadow.getElementById('uri-input');
    this.uriSpinner = shadow.getElementById('uri-spinner');
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
    if (!this.uriInput.value) {
      this.uriInput.focus();
    } else if (!this.titleInput.value) {
      this.titleInput.focus();
      this.onUriBlur();
    } else {
      this.descriptionInput.focus();
    }
  }

  async onUriBlur() {
    if (!this.uriInput.value) {
      return;
    }

    this.uriSpinner.hidden = false;
    this.uriSpinner.playing = true;
    let searchParams = new URLSearchParams({'uri': this.uriInput.value});
    console.log(`/bookmarks/page-info?${searchParams}`);
    let fetchUrl = new URL(`/bookmarks/page-info?${searchParams}`, document.URL);
    let r = await fetch(fetchUrl);
    this.uriSpinner.hidden = true;
    this.uriSpinner.playing = false;

    if (!r.ok) {
      return;
    }

    let pageInfo = await r.json();
    this.titleInput.value = pageInfo.title;
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
