// @flow

import /*:: ProgressSpinner from */ "../web_modules/elix/define/ProgressSpinner.js";
import { cast } from "../cms2/types.js";

const template = document.createElement('template');
template.innerHTML = `
  <link rel="stylesheet" type="text/css" href="/lib.css" />

  <form id="main-form" class="pure-form" method="post" action="/bookmarks">
    <fieldset>
      <legend>Edit Bookmark</legend>

      <label for="uri-input">URI:</label>
      <input name="uri" id="uri-input" type="text" placeholder="URI" required />
      <elix-progress-spinner id="uri-spinner" hidden></elix-progress-spinner>

      <label for="title-input">Title:</label>
      <input name="title" id="title-input" type="text" placeholder="Title" required />

      <label for="description-input">Description:</label>
      <textarea name="description" id="description-input" placeholder="Description"></textarea>

      <label for="via-input">Via:</label>
      <input name="via" id="via-input" type="text" placeholder="Source URI" />

      <label for="visibility-input">Visibility:</label>
      <select id="visibility-input" name="visibility" required>
        <option value="PUBLIC" selected>Public</option>
        <option value="SEMIPRIVATE">Semiprivate</option>
        <option value="PRIVATE">Private</option>
      </select>

      <div class="controls">
        <button type="submit" class="pure-button pure-button-primary">Submit Bookmark</button>
      </div>
    </fieldset>
  </form>`;

export class MlkBookmarkSubmissionForm extends HTMLElement {
  /*::
  mainForm: HTMLFormElement;
  descriptionInput: HTMLTextAreaElement;
  titleInput: HTMLInputElement;
  uriInput: HTMLInputElement;
  uriSpinner: ProgressSpinner;
  viaInput: HTMLInputElement;
  visibilityInput: HTMLInputElement;
  loaded: boolean;
  */

  constructor() {
    super();
    this.loaded = false;
  }

  static get observedAttributes() /*: Array<string>*/ {
    return [];
  }

  show() {
    this.createShadow();
  }

  createShadow() {
    if (this.shadowRoot !== null) {
      return;
    }

    let shadow = this.attachShadow({mode: "open"});
    shadow.appendChild(template.content.cloneNode(true));

    this.mainForm =
        cast(shadow.getElementById('main-form'));
    this.descriptionInput =
        cast(shadow.getElementById('description-input'));
    this.titleInput =
        cast(shadow.getElementById('title-input'));
    this.uriInput =
        cast(shadow.getElementById('uri-input'));
    this.uriSpinner =
        cast(shadow.getElementById('uri-spinner'));
    this.viaInput =
        cast(shadow.getElementById('via-input'));
    this.visibilityInput =
        cast(shadow.getElementById('visibility-input'));

    if (this.editedId !== null) {
      this.mainForm.method = "post";
      this.mainForm.action = `/bookmarks/${this.editedId}/edit`;
    }

    this.uriInput.addEventListener('blur', this.onUriBlur.bind(this));
    this.uriInput.value = this.uri || "";
    this.titleInput.value = this.titleText || "";
    this.descriptionInput.innerText = this.description || "";
    this.viaInput.value = this.via || "";
  }

  get editedId() /*:number | null*/ {
    let attr = this.getAttribute("edited-id");
    if (attr === null) {
      return null;
    }

    return parseInt(attr, 10);
  }

  get isEditor() /*: boolean*/ {
    return this.editedId !== null;
  }

  connectedCallback() {}

  disconnectedCallback () {
    this.uriInput.removeEventListener('blur', this.onUriBlur.bind(this));
  }

  attributeChangedCallback(name /*:string*/, oldValue /*:string*/, newValue /*:string*/) {
  }

  get uri() /*: ?string*/ {
    return this.getAttribute("uri");
  }

  get titleText() /*: ?string*/ {
    return this.getAttribute("title");
  }

  get description() /*: ?string*/ {
    return this.getAttribute("description");
  }

  get via() /*: ?string*/ {
    return this.getAttribute("via");
  }

  focus() {
    this.show();
    if (!this.uriInput.value) {
      this.uriInput.focus();
    } else if (!this.titleInput.value) {
      this.titleInput.focus();
      this.onUriBlur();
    } else {
      this.descriptionInput.focus();
    }
    this.load();
  }

  async onUriBlur() {
    if (this.isEditor || !this.uriInput.value) {
      return;
    }

    this.uriSpinner.hidden = false;
    this.uriSpinner.playing = true;
    let searchParams = new URLSearchParams({'uri': this.uriInput.value});
    let fetchUrl = new URL(`/bookmarks/page-info?${searchParams.toString()}`, document.URL);
    let r = await fetch(fetchUrl, {headers: {"accept": "application/json"}});
    this.uriSpinner.hidden = true;
    this.uriSpinner.playing = false;

    if (!r.ok) {
      return;
    }

    let pageInfo = await r.json();
    this.titleInput.value = pageInfo.title;
  }

  async load() {
    if (this.editedId === null || this.loaded) {
      return;
    }

    let fetchUrl = new URL(`/posts/${this.editedId}`, document.URL);
    let r = await fetch(fetchUrl, {headers: {"accept": "application/json"}});

    if (!r.ok) {
      return;
    }

    let post = await r.json();
    this.uriInput.value = post.uri;
    this.viaInput.value = post.via || "";
    this.visibilityInput.value = post.visibility;
    if (post.texts['']) {
      this.titleInput.value = post.texts[''].title;
      this.descriptionInput.value = post.texts[''].description;
    }

    this.loaded = true;
  }
}

customElements.define("mlk-bookmark-submission-form", MlkBookmarkSubmissionForm);
