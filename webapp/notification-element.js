/**
 * @license
 * Copyright (c) 2020 Selcuk SERT. All rights reserved.
 */

import { LitElement, html, css } from 'lit-element';
import '@polymer/iron-icons/iron-icons';
import '@polymer/paper-icon-button/paper-icon-button';
import '@polymer/paper-styles/color';
import '@polymer/paper-styles/typography';
import '@polymer/app-layout/app-toolbar/app-toolbar';
import '@polymer/paper-toggle-button/paper-toggle-button';

/**
 * An example element.
 *
 * @slot - This element has a slot
 * @csspart button - The button
 */
export class NotificationElement extends LitElement {
  static get styles() {
    return [
      css`
      :host {
        /**
         * The width for the expandible item is:
         * ((100% - subPixelAdjustment) / columns * itemColumns - gutter
         *
         * - subPixelAdjustment: 0.1px (Required for IE 11)
         * - gutter: var(--app-grid-gutter)
         * - columns: var(--app-grid-columns)
         * - itemColumn: var(--app-grid-expandible-item-columns)
         */
        --app-grid-expandible-item: {
          -webkit-flex-basis: calc((100% - 0.1px) / var(--app-grid-columns, 1) * var(--app-grid-expandible-item-columns, 1) - var(--app-grid-gutter, 0px)) !important;
          flex-basis: calc((100% - 0.1px) / var(--app-grid-columns, 1) * var(--app-grid-expandible-item-columns, 1) - var(--app-grid-gutter, 0px)) !important;
          max-width: calc((100% - 0.1px) / var(--app-grid-columns, 1) * var(--app-grid-expandible-item-columns, 1) - var(--app-grid-gutter, 0px)) !important;
        };
      }

      .app-grid {
        display: -ms-flexbox;
        display: -webkit-flex;
        display: flex;

        -ms-flex-direction: row;
        -webkit-flex-direction: row;
        flex-direction: row;

        -ms-flex-wrap: wrap;
        -webkit-flex-wrap: wrap;
        flex-wrap: wrap;

        padding-top: var(--app-grid-gutter, 0px);
        padding-left: var(--app-grid-gutter, 0px);
        box-sizing: border-box;
      }

      .app-grid > * {
        /* Required for IE 10 */
        -ms-flex: 1 1 100%;
        -webkit-flex: 1;
        flex: 1;

        /* The width for an item is: (100% - subPixelAdjustment - gutter * columns) / columns */
        -webkit-flex-basis: calc((100% - 0.1px - (var(--app-grid-gutter, 0px) * var(--app-grid-columns, 1))) / var(--app-grid-columns, 1));
        flex-basis: calc((100% - 0.1px - (var(--app-grid-gutter, 0px) * var(--app-grid-columns, 1))) / var(--app-grid-columns, 1));

        max-width: calc((100% - 0.1px - (var(--app-grid-gutter, 0px) * var(--app-grid-columns, 1))) / var(--app-grid-columns, 1));
        margin-bottom: var(--app-grid-gutter, 0px);
        margin-right: var(--app-grid-gutter, 0px);
        height: var(--app-grid-item-height);
        box-sizing: border-box;
      }

      .app-grid[has-aspect-ratio] > * {
        position: relative;
      }

      .app-grid[has-aspect-ratio] > *::before {
        display: block;
        content: "";
        padding-top: var(--app-grid-item-height, 100%);
      }

      .app-grid[has-aspect-ratio] > * > * {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
      }
      `,
      css`
      :host {
        display: block;
        --app-grid-columns: 4;
        --app-grid-gutter: 5px;
        --app-grid-expandible-item-columns: 4;
      }

      app-toolbar {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        background: var(--google-blue-700);
        box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.3);
        color: white;
        z-index: 1;
        font-weight: 350;
        font-size: 18px;
      }

      .consentItem {
        margin: 16px 16px 0 16px;
        padding: 15px;
        border-radius: 8px;
        background-color: white;
        border: 1px solid #ddd;

        @apply --layout-horizontal;
      }

      .avatar {
        height: 40px;
        width: 40px;
        border-radius: 20px;
        box-sizing: border-box;
        background-color: #DDD;
      }

      .pad {
        padding: 0 16px;
        @apply --layout-flex;
        @apply --layout-vertical;
      }

      .primary {
        font-size: 16px;
        font-weight: bold;
      }

      .secondary {
        font-size: 14px;
        margin: 3px 0;
      }

      #consent {
        padding-top: 64px;
        padding-bottom: 16px;
        max-width: 1200px;
        margin: auto;
      }
    `]
  }

  static get properties() {
    return {
      consentData: {
        type: Array
      }
    };
  }

  constructor() {
    super();
  }

  render() {
    return html`
      <app-toolbar>
        <div main-title>Notification Consent Settings</div>
        <paper-icon-button @click=${this.fetchAllConsentData} icon="refresh"></paper-icon-button>
        <paper-icon-button @click=${this.resetConsentData} icon="restore-page"></paper-icon-button>
      </app-toolbar>

      <div id="consent">
      ${this.consentData.map(consent =>
      html`
        <div>
          <div class="consentItem">
            <div class="pad">
              <div class="app-grid">
                <div class="primary">${consent.recipient}</div>
                <div>${new Date(consent.consentDate).toLocaleString("tr-TR")}</div>
                <div>${consent.type}</div>
                <div><paper-toggle-button ?checked=${consent.status === "Onay"} @change=${this.toggleChanged}></paper-toggle-button></div>
              </div>
            </div>
          </div>
        </div>
      `)
      }
      </div>
    `;
  }

  connectedCallback() {
    super.connectedCallback();
    this.fetchAllConsentData();
  }

  fetchAllConsentData() {
    fetch('http://localhost:8080/consent/all')
      .then(res => res.json())
      .then(response => {
        this.consentData = response;
      });
  }

  changeConsent(r, a) {
    let formData = new FormData();
    formData.append('recipient', r);
    formData.append('approved', a);

    fetch('http://localhost:8080/consent', {
      method: 'POST',
      body: formData
    })
    .then(res => res.json())
    .then(response => {
      this.consentData = response;
    });
  }

  resetConsentData() {
    let count = 10;
    let formData = new FormData();
    formData.append('count', count);

    fetch('http://localhost:8080/consent/reset', {
      method: 'POST',
      body: formData
    })
    .then(res => res.json())
    .then(response => {
      this.consentData = response;
    });
}

  toggleChanged(e) {
    let recipient = e.path[2].children[0].innerText
    let approved = e.path[0].checked;

    //console.log("recipient: %s | approved: %s", recipient, approved);

    this.changeConsent(recipient, approved);
  }
}

window.customElements.define('notification-element', NotificationElement);
