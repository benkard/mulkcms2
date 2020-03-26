// @flow

import /*:: ProgressSpinner from */ "../web_modules/elix/define/ProgressSpinner.js";
import { cast } from "../cms2/types.js";

const template = document.createElement('template');
template.innerHTML = `
  <link rel="stylesheet" type="text/css" href="/cms2/base.css" />
  <link rel="stylesheet" type="text/css" href="/lazychat/MlkLazychatSubmissionForm.css" />

  <form class="pure-form" method="post" action="/lazychat">
    <fieldset>
      <legend>New Message</legend>

      <label for="text-input">Text:</label>
      <textarea name="text" id="text-input" placeholder="Text"></textarea>

      <label for="visibility-input">Visibility:</label>
      <select id="visibility-input" name="visibility" required>
        <option value="public" selected>Public</option>
        <option value="semiprivate">Semiprivate</option>
        <option value="private">Private</option>
      </select>

      <div class="controls">
        <button type="submit" class="pure-button pure-button-primary">Submit Message</button>
      </div>
    </fieldset>
  </form>`;

export class MlkLazychatSubmissionForm extends HTMLElement {
  /*::
  textInput: HTMLTextAreaElement;
  */

  constructor() {
    super();

    let shadow = this.attachShadow({mode: "open"});
    shadow.appendChild(template.content.cloneNode(true));

    this.textInput =
        cast(shadow.getElementById('text-input'));
  }

  static get observedAttributes() {
    return [];
  }

  connectedCallback () {}

  disconnectedCallback () {}

  attributeChangedCallback(name /*:string*/, oldValue /*:string*/, newValue /*:string*/) {}

  focus() {
    this.textInput.focus();
  }
}

customElements.define("mlk-lazychat-submission-form", MlkLazychatSubmissionForm);
