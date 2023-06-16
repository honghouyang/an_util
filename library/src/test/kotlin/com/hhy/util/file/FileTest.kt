package com.hhy.util.file

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.config.Consts
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * 文件目录结构：
 * file-a
 * -- dir
 * ---- file-b
 * ---- file-c
 * ---- sub
 * ------ file-d
 * ---- empty-sub
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class FileTest {
    lateinit var app: Application
    private lateinit var fileA: File
    private lateinit var fileB: File
    private lateinit var fileC: File
    private lateinit var fileD: File
    private lateinit var dir: File
    private lateinit var subDir: File
    private lateinit var emptySubDir: File
    private lateinit var targetPath: String

    companion object {
        const val FILE_A_NAME = "file-a"
        const val FILE_A_CONTENT = "Hello, kotlin!"
        const val FILE_B_NAME = "file-b"
        const val FILE_B_CONTENT = "I love kotlin."
        const val FILE_C_NAME = "file-c"
        const val FILE_C_CONTENT = "Bye, java."
        const val FILE_D_NAME = "file-d"
        const val FILE_D_CONTENT = "See you later."
        const val DIR_NAME = "language"
        const val SUB_DIR_NAME = "sub"
        const val EMPTY_SUB_DIR_NAME = "empty-sub"
        const val TARGET_DIR_NAME = "target"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        app = ApplicationProvider.getApplicationContext()
        app.injectAsAppCtx()
        targetPath =
            appCtx.getExternalFilesDir(null)?.absolutePath + File.separator + TARGET_DIR_NAME

        fileA = File(appCtx.getExternalFilesDir(null), FILE_A_NAME)
        fileA.create()
        fileA.writeText(FILE_A_CONTENT)

        dir = File(appCtx.getExternalFilesDir(null), DIR_NAME)
        dir.mkdirs()

        fileB = File(dir, FILE_B_NAME)
        fileB.create()
        fileB.writeText(FILE_B_CONTENT)

        fileC = File(dir, FILE_C_NAME)
        fileC.create()
        fileC.writeText(FILE_C_CONTENT)

        subDir = File(dir, SUB_DIR_NAME)
        subDir.mkdirs()

        fileD = File(subDir, FILE_D_NAME)
        fileD.create()
        fileD.writeText(FILE_D_CONTENT)

        emptySubDir = File(dir, EMPTY_SUB_DIR_NAME)
        emptySubDir.mkdirs()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun fileIsExist_shouldReturnFalse_whenFileNull() {
        val file: File? = null
        assertFalse { file.isExist() }
    }

    @Test
    fun fileIsExist_shouldReturnSameValueAsExist() {
        val file: File = mockk {
            every { exists() } returns true andThen false
        }
        assertTrue(file.isExist())
        assertFalse { file.isExist() }
        verify(exactly = 2) { file.exists() }
    }

    @Test
    fun createFile_shouldReturnTrue_whenFileAlreadyExist() {
        val file: File = mockk {
            every { exists() } returns true
        }
        assertTrue(file.create())
        verify { file.exists() }
        verify(exactly = 0) { file.createNewFile() }
    }

    @Test
    fun createFile_shouldReturnFalse_whenParentDirNull() {
        val file = mockk<File>()
        every { file.parentFile } returns null
        assertFalse { file.create() }
    }

    @Test
    fun createFile_shouldCreateParentDir_whenDirNotExist() {
        val file: File = mockk {
            every { exists() } returns false andThen true
            every { parentFile?.exists() } returns false
            every { parentFile?.mkdirs() } returns true
            every { createNewFile() } returns true
        }
        assertTrue(file.create())
        verify(exactly = 2) { file.exists() }
        verify { file.parentFile?.exists() }
        verify { file.parentFile?.mkdirs() }
        verify { file.createNewFile() }
    }

    @Test
    fun createFile_shouldReturnFalse_whenDirCreateFailure() {
        val file: File = mockk {
            every { exists() } returns false andThen true
            every { parentFile?.exists() } returns false
            every { parentFile?.mkdirs() } returns false
        }
        assertFalse(file.create())
        verify {
            file.exists()
            file.parentFile?.exists()
            file.parentFile?.mkdirs()
        }
    }

    @Test
    fun createFile_shouldNotCreateDir_whenDirAlreadyExist() {
        val file: File = mockk {
            every { exists() } returns false andThen true
            every { parentFile?.exists() } returns true
            every { createNewFile() } returns true
        }
        assertTrue(file.create())
        verify(exactly = 2) { file.exists() }
        val parentFile = file.parentFile
        verify(exactly = 0) {
            parentFile?.mkdirs()
        }
        verify { file.createNewFile() }
    }

    @Test
    fun calFileSize_shouldReturn0_whenIsNotFile() {
        val file: File = mockk {
            every { isDirectory } returns false
            every { isFile } returns false
        }
        assertEquals(0, file.calSize())
        verify {
            file.isDirectory
            file.isFile
        }
    }

    @Test
    fun calFileSize_shouldReturnFileSize_whenIsFile() {
        val length: Long = 100
        val file: File = mockk {
            every { isDirectory } returns false
            every { isFile } returns true
            every { length() } returns length
        }
        assertEquals(length, file.calSize())
        verify {
            file.isDirectory
            file.isFile
            file.length()
        }
    }

    @Test
    fun calFileSize_shouldReturn0_whenIsDirAndHasNoChild() {
        val length: Long = 10
        val dir: File = mockk {
            every { isDirectory } returns true
            every { listFiles() } returns null
            every { length() } returns length
        }
        assertEquals(0, dir.calSize())
        verify {
            dir.isDirectory
            dir.listFiles()
        }
        verify(exactly = 0) { dir.length() }
    }

    @Test
    fun calFileSize_shouldReturnDirSize_whenIsDirAndHasChild() {
        val childLength1: Long = 100
        val childFile1: File = mockk {
            every { isDirectory } returns false
            every { isFile } returns true
            every { length() } returns childLength1
        }

        val childLength2: Long = 200
        val childFile2: File = mockk {
            every { isDirectory } returns false
            every { isFile } returns true
            every { length() } returns childLength2
        }

        val childFile3: File = mockk {
            every { isDirectory } returns true
            every { isFile } returns false
            every { listFiles() } returns arrayOf(childFile2)
        }

        val parentFile: File = mockk {
            every { isDirectory } returns true
            every { isFile } returns false
            every { listFiles() } returns arrayOf(childFile1, childFile3)
        }

        assertEquals(childLength1 + childLength2, parentFile.calSize())
        every {
            childFile1.isDirectory
            childFile1.isFile
            childFile1.length()
        }
        every {
            childFile2.isDirectory
            childFile2.isFile
            childFile2.length()
        }
        every {
            childFile3.isDirectory
            childFile3.isFile
            childFile3.listFiles()
        }
        every {
            parentFile.isDirectory
            parentFile.isFile
            parentFile.listFiles()
        }
    }

    @Test
    fun fileZip_shouldThrowException_whenParentFileIllegal() {
        val file = mockk<File> {
            every { absolutePath } returns ""
        }
        assertFailsWith<IllegalArgumentException> { file.zip() }
        verify { file.absolutePath }
    }

    @Test
    fun fileZip_shouldThrowException_whenChildFileIllegal() {
        val file = mockk<File> {
            every { absolutePath } returns fileA.absolutePath
            every { isDirectory } returns true
            every { listFiles() } returns null
        }
        assertFailsWith<IllegalArgumentException> { file.zip() }
        verify {
            file.absolutePath
            file.isDirectory
            file.listFiles()
        }
    }

    @Test
    fun fileZipAndUnzip_shouldWorksCoupled_whenIsFile() {
        val zipFile = fileA.zip()
        assertTrue {
            zipFile.isExist() && fileA.isExist()
        }
        fileA.delete()
        assertFalse { fileA.isExist() }
        val unzipFile = zipFile.unzip()
        assertTrue {
            unzipFile.isExist()
        }
        val text = unzipFile.readText()
        assertEquals(FILE_A_CONTENT, text)
        assertEquals(FILE_A_NAME, unzipFile.name)
    }

    @Test
    fun fileZipAndUnzip_shouldWorksCoupled_whenIsDir() {
        val zipFile = dir.zip()
        assertTrue {
            zipFile.isExist() && dir.isExist()
        }
        dir.deleteRecursively()
        assertFalse {
            dir.isExist()
        }
        val unzipFile = zipFile.unzip()
        assertTrue {
            unzipFile.isExist()
        }

        assertEquals(
            FILE_B_CONTENT,
            unzipFile.listFiles { _, name -> name == FILE_B_NAME }?.get(0)?.readText()
        )

        assertEquals(
            FILE_C_CONTENT,
            unzipFile.listFiles { _, name -> name == FILE_C_NAME }?.get(0)?.readText()
        )

        assertEquals(
            FILE_D_CONTENT,
            unzipFile.listFiles { _, name -> name == SUB_DIR_NAME }?.get(0)
                ?.listFiles { _, name -> name == FILE_D_NAME }?.get(0)?.readText()
        )
    }

    @Test
    fun fileZip_shouldInSameDirAndSourceExist_whenNoParams() {
        val fileAParent = fileA.parentFile
        val zipFile = fileA.zip()
        assertTrue {
            zipFile.isExist() && fileA.isExist()
        }
        assertEquals(fileAParent, zipFile.parentFile)

        val dirParent = dir.parentFile
        val zipDir = dir.zip()
        assertTrue {
            zipDir.isExist() && dir.isExist()
        }
        assertEquals(dirParent, zipDir.parentFile)
    }

    @Test
    fun fileZip_shouldRemoveSource_whenCleanIsTrue() {
        val zipFile = fileA.zip(clean = true)
        assertTrue {
            zipFile.isExist() && !fileA.isExist()
        }

        val zipDir = dir.zip(clean = true)
        assertTrue {
            zipDir.isExist() && !dir.isExist()
        }
    }

    @Test
    fun fileZip_shouldInSpecifiedPath_whenPathIsRight() {
        val zipFilePath = targetPath + File.separator + FILE_A_NAME + Consts.ZIP_EXT
        val zipFile = fileA.zip(zipFilePath)
        assertTrue {
            zipFile.isExist() && fileA.isExist()
        }
        assertEquals(zipFilePath, zipFile.absolutePath)

        val zipDirPath = targetPath + File.separator + DIR_NAME + Consts.ZIP_EXT
        val zipDir = dir.zip(zipDirPath)
        assertTrue {
            zipDir.isExist() && dir.isExist()
        }
        assertEquals(zipDirPath, zipDir.absolutePath)
    }

    @Test
    fun fileUnZip_shouldThrowException_whenParentFileIllegal() {
        val file = mockk<File> {
            every { parent } returns null
        }
        assertFailsWith<IllegalArgumentException> { file.unzip() }
        verify { file.parent }
    }

    @Test
    fun fileUnzip_shouldRemoveSource_whenCleanIsTrue() {
        val zipFile = fileA.zip(clean = true)
        assertTrue {
            zipFile.isExist() && !fileA.isExist()
        }
        fileA.delete()
        assertFalse {
            fileA.isExist()
        }
        zipFile.unzip(clean = true)
        assertFalse {
            zipFile.isExist()
        }

        val zipDir = dir.zip(clean = true)
        assertTrue {
            zipDir.isExist() && !dir.isExist()
        }
        dir.deleteRecursively()
        assertFalse {
            dir.isExist()
        }
        zipDir.unzip(clean = true)
        assertFalse {
            zipDir.isExist()
        }
    }

    @Test
    fun fileUnzip_shouldInSpecifiedPath_whenPathIsRight() {
        val zipFile = fileA.zip()
        assertTrue {
            zipFile.isExist() && fileA.isExist()
        }
        fileA.delete()
        assertFalse {
            fileA.isExist()
        }
        val unzipFile = zipFile.unzip(targetPath)
        assertTrue {
            unzipFile.isExist()
        }
        assertEquals(targetPath + File.separator + fileA.name, unzipFile.absolutePath)

        val zipDir = dir.zip()
        assertTrue {
            zipDir.isExist() && dir.isExist()
        }
        dir.deleteRecursively()
        assertFalse {
            dir.isExist()
        }
        val unzipDir = zipDir.unzip(targetPath)
        assertTrue {
            unzipDir.isExist()
        }
        assertEquals(targetPath + File.separator + dir.name, unzipDir.absolutePath)
    }
}
