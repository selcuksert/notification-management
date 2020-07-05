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
import '@polymer/paper-spinner/paper-spinner';
import { gridStyles } from './styles/app-grid';

/**
 * An example element.
 *
 * @slot - This element has a slot
 * @csspart button - The button
 */
export class NotificationElement extends LitElement {
  static get styles() {
    return [
      gridStyles,
      css`
      :host {
        display: block;
        --app-grid-columns: 3;
        --app-grid-expandible-item-columns: 3;
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
        font-weight: 300;
        font-size: 18px;
      }

      .loadingIndicator {
        text-align: center;
        height: 40px;
      }

      paper-spinner {
        width: 40px;
        height: 40px;
      }

      .recipient {
        font-size: 16px;
        font-weight: bold;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;      
      }

      #consent {
        padding-top: 64px;
        padding-bottom: 16px;
        max-width: 1200px;
        margin: auto;
      }

      .consentItem {
        margin: 16px 16px 0 16px;
        padding: 15px;
        border-radius: 8px;
        background-color: white;
        border: 1px solid #ddd;

        @apply --layout-horizontal;
      }

      .pad {
        padding: 0 16px;
        @apply --layout-flex;
        @apply --layout-vertical;
      }
    `]
  }

  static get properties() {
    return {
      consentData: {
        type: Array
      },
      consentDataUpdated: {
        type: Boolean
      },
      fetching: {
        type: Boolean
      }
    };
  }

  constructor() {
    super();
    this.consentData = [];
    this.fetching = false;
  }

  render() {
    return html`
      <app-toolbar>
        <div main-title>Notification Consent Settings</div>
        <paper-icon-button @click=${this.fetchAllConsentData} icon="refresh"></paper-icon-button>
        <paper-icon-button @click=${this.clearConsentData} icon="delete"></paper-icon-button>
        <paper-icon-button @click=${this.generateConsentData} icon="add"></paper-icon-button>
      </app-toolbar>

      <div id="consent">
      ${this.consentData.map(consent =>
      html`
        <div>
          <div class="consentItem">
            <div class="pad">
              <div class="app-grid">
                <div class="recipient">${consent.recipient}</div>
                <div>${consent.source}</div>
                <div><paper-toggle-button ?checked=${consent.status === "Onay"} @change=${this.toggleChanged}></paper-toggle-button></div>
                <div>${consent.type}</div>
                <div>${consent.recipientType}</div>
                <div>${new Date(consent.consentDate).toLocaleString("tr-TR")}</div>
              </div>
            </div>
          </div>
        </div>
      `)
      }
      </div>

      <div class="loadingIndicator">
        <paper-spinner ?active=${this.fetching}></paper-spinner>
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
        if (response) {
          this.consentData = response;
        }
      })
      .catch(e => console.log("Error: ", e))
      .finally(() => this.fetching = false);
  }

  changeConsent(r, a) {
    let formData = new FormData();
    formData.append('recipient', r);
    formData.append('approved', a);

    fetch('http://localhost:8080/consent', {
      method: 'PATCH',
      body: formData
    })
      .then(res => res.json())
      .then(response => {
        this.consentData = response;
      })
      .catch(e => console.log("Error: ", e));
  }

  clearConsentData() {
    this.consentDataUpdated = false;
    this.consentData = [];

    fetch('http://localhost:8080/consent', {
      method: 'DELETE'
    })
      .then(response => this.consentDataUpdated = response.ok)
      .catch(e => console.log("Error: ", e));
  }


  generateConsentData() {
    let count = 10;
    let formData = new FormData();
    formData.append('count', count);
    this.consentDataUpdated = false;
    this.consentData = [];

    fetch('http://localhost:9000/mdm/send', {
      method: 'POST',
      body: formData
    })
      .then(response => this.consentDataUpdated = response.ok)
      .catch(e => console.log("Error: ", e));
  }

  toggleChanged(e) {
    let recipient = e.path[2].children[0].innerText
    let approved = e.path[0].checked;

    //console.log("recipient: %s | approved: %s", recipient, approved);

    this.changeConsent(recipient, approved);
  }

  updated(changedProperties) {
    changedProperties.forEach((oldValue, propName) => {
      if (propName === "consentDataUpdated" && oldValue === false) {
        this.fetching = true;
        setTimeout(() => {
          this.fetchAllConsentData();
          this.fetching = false;
        }, 1000);
      }
    });
  }

}

window.customElements.define('notification-element', NotificationElement);
