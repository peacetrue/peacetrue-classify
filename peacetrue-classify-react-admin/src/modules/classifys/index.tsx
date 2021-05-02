import * as React from "react";
import {Resource} from "react-admin";

import {ClassifyList} from './List';
import {ClassifyCreate} from './Create';
import {ClassifyEdit} from './Edit';
import {ClassifyShow} from './Show';
import BusinessIcon from '@material-ui/icons/Business';

export const Classify = {
  list: ClassifyList,
  create: ClassifyCreate,
  edit: ClassifyEdit,
  show: ClassifyShow
};
export const ClassifyResource = <Resource icon={BusinessIcon} name="classifys" {...Classify} />;
export default ClassifyResource;
export * from "./Messages"
export * from "./GenerateButton"
export * from "./RearrangeButton"
export * from "./ChildrenButton"
