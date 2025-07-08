interface Window {
  ethereum?: {
    request: (args: { method: string }) => Promise<any>;
        //add more ethereum API typings here if needed
  };
}
