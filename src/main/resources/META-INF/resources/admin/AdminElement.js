import {html, render} from "../node_modules/lit-html/lit-html.js";


export class AdminElement extends HTMLElement {
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

    onSwitchClicked = (event) => {
        this.setAttribute("greetee", "Andreas");
        this.render();
    };

    render() {
        let greetee = this.getAttribute("greetee");

        const template = html`
            <p>Hello ${greetee}! <button @click=${this.onSwitchClicked}>Switch</button></p>
            `;

        render(template, this.shadowRoot);
    }
}

customElements.define("mlk-admin", AdminElement);
