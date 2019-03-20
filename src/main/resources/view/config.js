import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';
import { ToggleModule, toggleOptions } from './toggle-module/ToggleModule.js'
import { EndScreenModule } from './endscreen-module/EndScreenModule.js';

export const modules = [
	GraphicEntityModule, TooltipModule, EndScreenModule,
	ToggleModule
];

export const options = [
  ...toggleOptions
]

export const playerColors = ['#ff1d5c', '#22a1e4'];