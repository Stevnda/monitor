export const createFileChunk = (file: File) => {
  const size = 5242880 * 4;
  const fileChunkList = [];
  let count = 0;
  while (count < file.size) {
    fileChunkList.push(file.slice(count, count + size));
    count += size;
  }
  return fileChunkList;
};

