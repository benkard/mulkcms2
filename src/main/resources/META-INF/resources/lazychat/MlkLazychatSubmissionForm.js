// @flow

import /*:: ProgressSpinner from */ "../web_modules/elix/define/ProgressSpinner.js";
import { cast } from "../cms2/types.js";

const template = document.createElement('template');
template.innerHTML = `
  <link rel="stylesheet" type="text/css" href="/cms2/base.css" />
  <link rel="stylesheet" type="text/css" href="/lazychat/MlkLazychatSubmissionForm.css" />

  <form id="main-form" class="pure-form" method="post" action="/lazychat">
    <fieldset>
      <legend>Edit Message</legend>

      <label for="text-input">Text:</label>
      <textarea name="text" id="text-input" placeholder="Text"></textarea>

      <label for="visibility-input">Visibility:</label>
      <select id="visibility-input" name="visibility" required>
        <option value="PUBLIC">Public</option>
        <option value="SEMIPRIVATE" selected>Semiprivate</option>
        <option value="PRIVATE">Private</option>
      </select>

      <div class="controls">
        <button type="submit" class="pure-button pure-button-primary">Submit Message</button>
      </div>
    </fieldset>
  </form>`;

export class MlkLazychatSubmissionForm extends HTMLElement {
  /*::
  mainForm: HTMLFormElement;
  textInput: HTMLTextAreaElement;
  visibilityInput: HTMLInputElement;
  loaded: boolean;
  */

  constructor() {
    super();

    let shadow = this.attachShadow({mode: "open"});
    shadow.appendChild(template.content.cloneNode(true));

    this.mainForm =
        cast(shadow.getElementById('main-form'));
    this.textInput =
        cast(shadow.getElementById('text-input'));
    this.visibilityInput =
        cast(shadow.getElementById('visibility-input'));

    this.loaded = false;
  }

  static get observedAttributes() {
    return [];
  }

  get editedId() /*:number | null*/ {
    let attr = this.getAttribute("edited-id");
    if (attr === undefined || attr === null) {
      return null;
    }
    return parseInt(attr);
  }

  get isEditor() {
    return this.editedId !== null;
  }

  connectedCallback() {
    if (this.editedId !== null) {
      this.mainForm.method = "post";
      this.mainForm.action = `/lazychat/${this.editedId}/edit`;
    }
  }

  disconnectedCallback() {}

  attributeChangedCallback(name /*:string*/, oldValue /*:string*/, newValue /*:string*/) {}

  focus() {
    this.textInput.focus();
    this.load();
  }

  async load() {
    if (this.editedId === null || this.loaded) {
      return;
    }

    let fetchUrl = new URL(`/posts/${this.editedId}`, document.URL);
    let r = await fetch(fetchUrl);

    if (!r.ok) {
      return;
    }

    let post = await r.json();
    this.textInput.value = post.content;
    this.visibilityInput.value = post.visibility;

    this.loaded = true;
  }
}

customElements.define("mlk-lazychat-submission-form", MlkLazychatSubmissionForm);
