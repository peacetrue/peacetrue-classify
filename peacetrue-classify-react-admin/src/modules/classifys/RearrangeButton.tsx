import * as React from "react";
import {Button, ButtonProps, useDataProvider, useNotify, useRefresh} from "react-admin";
import ReorderIcon from '@material-ui/icons/Reorder';

export const RearrangeButton = (_props: ButtonProps) => {
  let dataProvider = useDataProvider(),
    notify = useNotify(),
    refresh = useRefresh();
  let onClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    dataProvider.update('classifys/rearrange', {id: 1, data: {id: 1}, previousData: {id: 1}})
      .then((_data) => {
        notify('操作成功!');
        refresh();
      });
  };
  return (<Button label={'排序'} onClick={onClick}><ReorderIcon/></Button>)
}

export default RearrangeButton;
