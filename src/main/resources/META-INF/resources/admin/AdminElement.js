import {html, render} from "../web_modules/lit-html.js";
import {createStore} from "../web_modules/redux.js";

export class AdminElement extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: "open"});
        this.store = createStore(this.update.bind(this), {switched: false});
        this.store.subscribe(this.render.bind(this));
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

    onSwitchClicked(event) {
        this.store.dispatch({ type: "SWITCH" });
    }

    update(state, action) {
        console.log(`Processing action: ${JSON.stringify(action)}`);
        switch (action.type) {
            case "SWITCH":
                state.switched = !state.switched;
                break;
        }
        return state;
    }

    render() {
        let state = this.store.getState();
        let greetee = state.switched ? "Andreas" : this.getAttribute("greetee");

        const template = html`
            <p>Hello ${greetee}! <button @click=${this.onSwitchClicked.bind(this)}>Switch</button></p>
            `;

        render(template, this.shadowRoot);
    }
}

customElements.define("mlk-admin", AdminElement);
