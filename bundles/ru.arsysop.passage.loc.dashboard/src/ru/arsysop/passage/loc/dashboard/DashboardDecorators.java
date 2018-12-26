/*******************************************************************************
 * Copyright (c) 2018 ArSysOp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 *******************************************************************************/
package ru.arsysop.passage.loc.dashboard;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.graphics.Image;

public class DashboardDecorators {

	public static void decorateFeatureSets(long count, ControlDecoration decoration) {
		FieldDecorationRegistry registry = FieldDecorationRegistry.getDefault();
		if (count > 0) {
			Image image = registry.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage();
			decoration.setImage(image);
			String pattern = "You have %s Feature Set(s) defined.\nUse it define the Features";
			decoration.setDescriptionText(String.format(pattern, count));
		} else {
			Image image = registry.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
			decoration.setImage(image);
			decoration.setDescriptionText("You have no Feature Set defined.\nPlease create or load Feature Set(s)");
		}
	}

	public static void decorateFeatures(long count, ControlDecoration decoration) {
		FieldDecorationRegistry registry = FieldDecorationRegistry.getDefault();
		if (count > 0) {
			Image image = registry.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage();
			decoration.setImage(image);
			String pattern = "You have %s Feature(s) defined.\nUse it define the Feature Version(s)";
			decoration.setDescriptionText(String.format(pattern, count));
		} else {
			Image image = registry.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
			decoration.setImage(image);
			decoration.setDescriptionText("You have no Features defined.\nPlease create it in the Feature Set");
		}
	}

}
