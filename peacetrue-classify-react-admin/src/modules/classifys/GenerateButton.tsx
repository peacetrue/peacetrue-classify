import * as React from "react";
import {Button, ButtonProps, useCreate, useNotify} from "react-admin";
import BuildIcon from '@material-ui/icons/Build';

export const GenerateButton = (props: ButtonProps) => {
  const {record} = props;
  let parts = record?.code.split('-');
  let notify = useNotify();
  const [generate, {loading, error}] = useCreate('static-contents', {endpoint: parts[1], page: parts[2]});
  React.useEffect(() => {
    if (error) notify(error.message, 'error', false, false, 10 * 1000);
  }, [error])

  if (parts.length !== 3) return null;

  return (
    <Button label='生成' disabled={loading} onClick={(e) => {
      e.stopPropagation();
      generate();
    }}>
      <BuildIcon/>
    </Button>
  )
}

export default GenerateButton;
