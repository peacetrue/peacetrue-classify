import * as React from "react";
import {Button, ButtonProps, useListContext} from "react-admin";
import ExpandMoreSharpIcon from "@material-ui/icons/ExpandMoreSharp";

export const ChildrenButton = (props: ButtonProps) => {
  const {record} = props;
  const {
    filterValues,
    setFilters,
  } = useListContext();
  return record?.leaf ? null : (
    <Button label='下级' onClick={(e) => {
      e.stopPropagation();
      let filter = {...filterValues, parentId: record?.id};
      setFilters(filter, filter)
    }}>
      <ExpandMoreSharpIcon/>
    </Button>
  );
}

export default ChildrenButton;
