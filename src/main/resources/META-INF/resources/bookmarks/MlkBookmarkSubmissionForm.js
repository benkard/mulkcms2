import {html, render} from "../../web_modules/lit-html.js";
import ProgressSpinner from "../web_modules/elix/define/ProgressSpinner.js";

export class MlkBookmarkSubmissionForm extends HTMLElement {
  constructor() {
    super();
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

  focus() {
    let uriInput = this.shadowRoot.getElementById('uri-input');
    uriInput.focus();
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

      <form class="pure-form pure-form-aligned" method="post">
        <fieldset>
          <legend>New Bookmark</legend>

          <div class="pure-control-group">
            <label for="uri-input">URI:</label>
            <input name="uri" id="uri-input" type="text" placeholder="URI" required
                   @blur=${this.onUriBlur.bind(this)}/>
            <elix-progress-spinner id="uri-spinner" hidden></elix-progress-spinner>
          </div>

          <div class="pure-control-group">
            <label for="title-input">Title:</label>
            <input name="title" id="title-input" type="text" placeholder="Title" required/>
          </div>

          <div class="pure-control-group">
            <label for="description-input">Description:</label>
            <textarea name="description" id="description-input" placeholder="Description"></textarea>
          </div>

          <div class="pure-control-group">
            <label for="visibility-input">Visibility:</label>
            <select id="visibility-input" name="visibility" required>
              <option value="public">Public</option>
              <option value="semiprivate" selected>Semiprivate</option>
              <option value="private">Private</option>
            </select>
          </div>

          <div class="pure-controls">
            <button type="submit" class="pure-button pure-button-primary">Submit Bookmark</button>
          </div>
        </fieldset>
      </form>`;

    render(template, this.shadowRoot);
  }
}

customElements.define("mlk-bookmark-submission-form", MlkBookmarkSubmissionForm);
