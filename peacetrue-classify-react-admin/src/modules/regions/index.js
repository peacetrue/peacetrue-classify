import React from "react";
import {Resource} from "react-admin";

import {RegionList} from './list';
import {RegionCreate} from './create';
import {RegionEdit} from './edit';
import {RegionShow} from './show';

export const Region = {list: RegionList, create: RegionCreate, edit: RegionEdit, show: RegionShow};
const RegionResource = <Resource options={{label: '地区'}} name="regions" {...Region} />;
export default RegionResource;
