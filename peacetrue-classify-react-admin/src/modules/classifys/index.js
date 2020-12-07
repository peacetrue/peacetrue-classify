import React from "react";
import {Resource} from "react-admin";

import {ClassifyList} from './list';
import {ClassifyCreate} from './create';
import {ClassifyEdit} from './edit';
import {ClassifyShow} from './show';

export const Classify = {list: ClassifyList, create: ClassifyCreate, edit: ClassifyEdit, show: ClassifyShow};
const ClassifyResource = <Resource options={{label: '地区'}} name="regions" {...Classify} />;
export default ClassifyResource;
