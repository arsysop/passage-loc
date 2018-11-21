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
package ru.arsysop.passage.licensing.operator.workbench.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class PassageUI {

  public static GridData getButtonLayoutData() {
    GridData dataButton = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    dataButton.heightHint = 30;
    dataButton.widthHint = 120;
    return dataButton;
  }

}
