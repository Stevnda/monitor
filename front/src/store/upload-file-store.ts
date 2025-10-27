import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import { UploadRecord, ResponseType } from "@/type";
import {
  getUploadRecord,
  uploadChunks,
  mergeChunks,
  delRecord,
} from "@/api/request";
import { v4 as uuidv4 } from "uuid";

export const useUploadFileStore = defineStore("uploadFile", () => {
  const fileList = ref<{ file: File; parentId: string }[]>([]);
  let uploadingObj = reactive<{
    [key: string]: {
      file: File;
      finished: number;
      parentId: string;
      cancel: boolean;
    };
  }>({});
  const uploadedList = ref<UploadRecord[]>([]);
  const uploading = ref(false);

  const setUploadedList = async () => {
    uploadedList.value = [];
    const res = await getUploadRecord();
    if (res && res.code === 0) {
      uploadedList.value = res.data;
    }
  };

  const addFileList = (item: { file: File; finished: 0; parentId: string }) => {
    fileList.value.push(item);
  };

  const deleteUploaded = async (id: string, index: number) => {
    const res = await delRecord(id);
    if (res && res.code === 0) {
      uploadedList.value.splice(index, 1);
    }
  };

  const deleteUploading = (id: string) => {
    uploadingObj[id].cancel = true;
  };

  const executeUpload = () => {
    uploading.value = true;
    const handle = () => {
      return new Promise((resolve, reject) => {
        console.log(fileList.value.length);
        if (fileList.value.length) {
          const id = uuidv4();
          const f = fileList.value.shift()!;
          const file = {
            ...f,
            finished: 0,
            cancel: false,
          };
          uploadingObj[id] = file;
          uploadChunksHandle(file, id)
            .then(async (res) => {
              if (res && res.code === 0) {
                delete uploadingObj[id];
                uploadedList.value.unshift(res.data);
              }
              const result = await handle();
              if (result === 1) resolve(result);
              else reject();
            })
            .catch(() => {
              delete uploadingObj[id];
              reject();
            });
        } else {
          console.log("haha");
          resolve(1);
        }
      });
    };
    const promiseList = [];
    for (let i = 0; i < 5; i++) promiseList.push(handle());
    Promise.all(promiseList).then((res) => {
      console.log(res);
      uploading.value = false;
    });
  };

  function createFileChunk(file: File) {
    const size = 5242880;
    const fileChunkList = [];
    let count = 0;
    while (count < file.size) {
      fileChunkList.push(file.slice(count, count + size));
      count += size;
    }
    return fileChunkList;
  }

  const uploadChunksHandle = (
    item: { file: File; finished: number; parentId: string; cancel: boolean },
    id: string
  ) => {
    const fileName = item.file.name;
    const chunkList = createFileChunk(item.file);
    return new Promise<ResponseType>((resolve, reject) => {
      let count = 0;
      const total = chunkList.length;
      const uid = uuidv4();
      const parentId = item.parentId;
      const handle = async () => {
        if (chunkList.length && !uploadingObj[id].cancel) {
          const formData = new FormData();
          formData.append("file", chunkList.shift()!);
          formData.append("number", count.toString());
          formData.append("id", uid);
          const res = await uploadChunks(formData);
          if (res && res.code === 0) {
            count++;
            uploadingObj[id].finished = Math.floor((count / total) * 100);

            handle();
          } else reject();
        } else {
          if (count === total) {
            const res = await mergeChunks({
              parentId,
              fileName,
              id: uid,
              total,
            });
            resolve(res!);
          } else reject();
        }
      };
      handle();
    });
  };

  return {
    fileList,
    uploadedList,
    uploading,
    uploadingObj,
    setUploadedList,
    addFileList,
    executeUpload,
    deleteUploaded,
    deleteUploading,
  };
});
