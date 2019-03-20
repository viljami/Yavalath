import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
//import { ToggleModule } from './toggle-module/ToggleModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';

export const modules = [
	GraphicEntityModule, TooltipModule, EndScreenModule,
	//ToggleModule,
];
/*
export const options = [
  {
    // The name displayed over the toggle
    title: 'DEBUG',
    // Getters and setters for the on/off state of your toggle
    get: function () {
      return toggles.debugToggle // replace "myToggle" by the name of the toggle you want to use
    },
    set: function (value) {
      toggles.debugToggle = value // replace "myToggle" by the name of the toggle you want to use
      ToggleModule.refreshContent()
    },
    // The labels for the on/off states of your toggle
    values: {
      'ON': true,
      'OFF': false
    }
  }
]

export const toggles = {
  debugToggle: true,
};
*/

export const playerColors = ['#ff1d5c', '#22a1e4'];