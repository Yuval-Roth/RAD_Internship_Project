import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/side-nav/src/vaadin-side-nav.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/form-layout/src/vaadin-form-layout.js';
import '@vaadin/dialog/src/vaadin-dialog.js';
import '@vaadin/password-field/src/vaadin-password-field.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import '@vaadin/virtual-list/src/vaadin-virtual-list.js';
import 'Frontend/generated/jar-resources/virtualListConnector.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/side-nav/src/vaadin-side-nav-item.js';
import '@vaadin/context-menu/src/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/form-layout/src/vaadin-form-item.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/grid/src/vaadin-grid.js';
import '@vaadin/grid/src/vaadin-grid-column.js';
import '@vaadin/grid/src/vaadin-grid-sorter.js';
import '@vaadin/checkbox/src/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.ts';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/scroller/src/vaadin-scroller.js';
import '@vaadin/grid/src/vaadin-grid-column-group.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/notification/src/vaadin-notification.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '33c83196bbe5f58b27ba781720cff1711e8dd3aa26b672655072d3399eb71e84') {
    pending.push(import('./chunks/chunk-c83a40f58e3ffa964b3d784233a1c77927d102fb34d18d3694d47e39cc6e929a.js'));
  }
  if (key === 'cc8b0f70368dfbfc2bc9995db38d87adb76347bc186406ea9cd88bc21944fe40') {
    pending.push(import('./chunks/chunk-1fce5f239c3dcbbdb8b2bfb3f7652112b6811b08623fa7cbae4d470923bedf5f.js'));
  }
  if (key === '5720fb0c63a55852ccd11b98121c42c89cc0a6acc12fd204d4879cde9b9ebc75') {
    pending.push(import('./chunks/chunk-c83a40f58e3ffa964b3d784233a1c77927d102fb34d18d3694d47e39cc6e929a.js'));
  }
  if (key === '1395a46ba81f89777b7a0c3056615771b0d187c04e32dd95f67170a7eba68de2') {
    pending.push(import('./chunks/chunk-1fce5f239c3dcbbdb8b2bfb3f7652112b6811b08623fa7cbae4d470923bedf5f.js'));
  }
  if (key === '7c680a885d32439087097f112f207dbe7071b486920d8d2d2c76fd52a2874d26') {
    pending.push(import('./chunks/chunk-8f33b3beade92a8460be428fdb88dc280dad353da2db0dc6d2200c28ba044e9f.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}